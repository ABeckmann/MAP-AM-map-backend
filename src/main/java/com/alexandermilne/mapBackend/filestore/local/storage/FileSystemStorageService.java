package com.alexandermilne.mapBackend.filestore.local.storage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public Path getStoragePath(UUID userId, String filename) {
		return this.rootLocation.resolve(userId.toString()).resolve(StringUtils.stripFilenameExtension(filename));
	}

	public Optional<Path> findStoragePathByFilename(String filename) throws IOException {
		String glob = String.format("glob:%s/*/%s/video.mp4",this.rootLocation.toString(), filename); //
		//match(glob, rootLocation.toString());


		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(glob);
		return Files.walk(Paths.get(this.rootLocation.toString())).filter(pathMatcher::matches).findFirst();

		//return this.rootLocation.resolve(userId.toString()).resolve(StringUtils.stripFilenameExtension(filename));
	}
//	public String match(String glob, String location) throws IOException {
//		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(glob);
//		Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {
//			@Override
//			public FileVisitResult visitFile(Path path,
//											 BasicFileAttributes attrs) throws IOException {
//				if (pathMatcher.matches(path)) {
//					System.out.println(path);
//				}
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFileFailed(Path file, IOException exc)
//					throws IOException {
//				return FileVisitResult.CONTINUE;
//			}
//		});
//		return null;
//	}

	@Override
	public Path getIconLink(UUID userId, String filename) {
		return getStoragePath(userId, filename).resolve("incons");
	}

	public Path getVideoLink(UUID userId, String filename) {
		return getStoragePath(userId, filename).resolve("video.mp4");
	}

	@Override
	public void store(UUID userId, MultipartFile file) {
		//TODO ensure that user exists
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new com.alexandermilne.mapBackend.filestore.local.storage.StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new com.alexandermilne.mapBackend.filestore.local.storage.StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				//System.out.println(this.rootLocation.resolve(StringUtils.stripFilenameExtension(filename)).resolve("video.mp4"));
				Path StoragePath = getStoragePath(userId, filename);
				Path vidPath = StoragePath.resolve("video.mp4");

				Files.createDirectories(StoragePath);
				Files.copy(inputStream, vidPath,
					StandardCopyOption.REPLACE_EXISTING);
				Path IconsStoragePath = StoragePath.resolve("incons");
				Files.createDirectories(IconsStoragePath);

				try {
					FFmpegFrameGrabber g = new FFmpegFrameGrabber(vidPath.toString());
					g.start();

					Java2DFrameConverter c = new Java2DFrameConverter();
					for (int i = 0 ; i < 200; i++) {
						ImageIO.write(c.convert(g.grabImage()), "png", new File(IconsStoragePath.resolve(i+"icon.png").toString()));
					}
					g.stop();
				}
				catch (FrameGrabber.Exception | IOException e) {
					throw new com.alexandermilne.mapBackend.filestore.local.storage.StorageException("Failed to store file " + filename, e);
				}

			}
		}
		catch (IOException e) {
			throw new com.alexandermilne.mapBackend.filestore.local.storage.StorageException("Failed to store file " + filename, e);
		}


	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadIconAsResource(UUID userId, String filename) {
		try {
			Path file = getIconLink(userId, filename).resolve("199icon.png"); //TODO some inteligent way to decide which icon to use (avoid it being just black!)
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	public boolean doesFileExist(UUID userId, String filename) {
		String glob = String.format("glob:%s/%s/%s/video.mp4",this.rootLocation.toString(), userId, filename); //
		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(glob);
		try {
			Optional<Path> path = Files.walk(Paths.get(this.rootLocation.toString())).filter(pathMatcher::matches).findFirst();
			if (path.isPresent()) {
				return true;

			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Resource loadVideoAsResource(String filename) {
		try {
			Path file = findStoragePathByFilename(filename).get();
			System.out.println(String.format("file path: %s", file.toString()));
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not find file: " + filename, e);
		}
	}

	@Override
	public Resource loadVideoAsResource(UUID userId,String filename) {
		try {
			Path filePath = getVideoLink(userId, filename);

			System.out.println(String.format("file path: %s", filePath.toString()));
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
