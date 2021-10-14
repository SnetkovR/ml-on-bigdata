with art as (
    select artist_lastfm, listeners_lastfm, tag
    from artists_ex
    lateral view explode(tags_lastfm) tag AS tag
),
popular_tags as (
    select tag, count(tag) as populatiry
    from (select explode(tags_lastfm) as tag from artists_ex) tags
    group by tag
    order by populatiry desc
    limit 10
)
select distinct artist_lastfm, listeners_lastfm
from art
inner join popular_tags on popular_tags.tag=art.tag
order by listeners_lastfm desc
limit 10