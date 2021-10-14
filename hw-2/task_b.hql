select tag 
from (SELECT tag, count(tag) as populatiry
      FROM (select explode(tags_lastfm) as tag from artists_ex) tags
      GROUP BY tag
      ORDER BY populatiry desc
      LIMIT 1
    ) t1
