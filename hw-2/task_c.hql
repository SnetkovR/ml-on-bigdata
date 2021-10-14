with art as (
    select artist_lastfm, listeners_lastfm, tag
    from artists_ex
    LATERAL VIEW explode(tags_lastfm) tag AS tag
),
popular_tags as (
    SELECT tag, count(tag) as populatiry
    FROM (select explode(tags_lastfm) as tag from artists_ex) tags
    GROUP BY tag
    ORDER BY populatiry desc
    LIMIT 10
)
select distinct artist_lastfm, listeners_lastfm
from art
inner join popular_tags on popular_tags.tag=art.tag
order by listeners_lastfm desc
limit 10