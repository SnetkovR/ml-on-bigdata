select tag 
from (select tag, count(tag) as populatiry
      from (select explode(tags_lastfm) as tag from artists_ex) tags
      group by tag
      order by populatiry desc
      limit 1) t1
