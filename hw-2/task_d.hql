select distinct artist_lastfm, scrobbles_lastfm
from artists
where country_lastfm rlike "Russia" and ambiguous_artist = false
order by scrobbles_lastfm desc
limit 10