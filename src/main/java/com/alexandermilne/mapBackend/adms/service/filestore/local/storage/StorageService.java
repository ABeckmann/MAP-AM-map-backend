package com.alexandermilne.mapBackend.adms.service.filestore.local.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	Path getStoragePath(UUID userId, String filename);

	Path getIconLink(UUID userId, String filename);


	void store(UUID userId, MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);
	boolean doesFileExist(UUID userId, String filename);
	Resource loadAsResource(String filename);
	Resource loadIconAsResource(UUID userId, String filename);

	Resource loadVideoAsResource(Path videoStorageLocation);

	Resource loadVideoAsResource(String filename);
	Resource loadVideoAsResource(UUID userId,String filename);

	void deleteAll();

}
