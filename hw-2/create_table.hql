create external table artists_ex (
    mbid string,
    artist_mb string,
    artist_lastfm string,
    country_mb string,
    country_lastfm string,
    tags_mb array<string>,
    tags_lastfm array<string>,
    listeners_lastfm int,
    scrobbles_lastfm int,
    ambiguous_artist boolean
)
ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','
COLLECTION items terminated by ';'
TBLPROPERTIES ("skip.header.line.count"="1");
LOCATION '/user/artists.csv';
