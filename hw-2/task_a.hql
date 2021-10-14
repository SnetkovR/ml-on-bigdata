select artist_mb, scrobbles_lastfm
from artists
order by scrobbles_lastfm desc
limit 1