CREATE TABLE "map_user" (
  "Id" uuid NOT NULL PRIMARY KEY,
  "username" varchar(100) NOT NULL,
  "userProfileImageLink" varchar(100),
  "userMoney" int
);

CREATE TABLE "video" (
  "Id" uuid NOT NULL PRIMARY KEY,
  "videoOwnerId" uuid NOT NULL REFERENCES "map_user" ("Id"),
  "title" varchar(100) NOT NULL,
  "localStorageLocation" varchar(100) NOT NULL
);

CREATE TABLE "userVideoLicence" (
  "Id" uuid NOT NULL PRIMARY KEY,
  "videoId" uuid NOT NULL REFERENCES "video" ("Id"),
  "licenceOwnerId" uuid REFERENCES "map_user" ("Id"),
  "price" int NOT NULL,
  "region" varchar(100) NOT NULL
);

INSERT INTO public.map_user(
	"Id", username, "userProfileImageLink", "userMoney")
	VALUES ('93bf9ae1-2a1f-47b1-83f6-e9fe4ba1710a', 'James', NULL, 100);

INSERT INTO public.map_user(
	"Id", username, "userProfileImageLink", "userMoney")
	VALUES ('6203c0f4-63d1-4d51-91f5-64de0ee6073d', 'Bob', NULL, 200);
