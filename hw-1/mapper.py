import sys

SEP = ','
OUT_SEP = '|'
_sum, square, _count = 0, 0, 0

for line in sys.stdin:
    line = line.strip()
    try:
        price = int(line.split(SEP)[-7])
    except IndexError:
        continue
    except ValueError:
        continue
    _sum += price
    square += price * price
    _count += 1

chunk_mean = _sum / _count
chunk_var = square / _count - chunk_mean * chunk_mean

print(OUT_SEP.join([str(1), str(_count), str(chunk_mean), str(chunk_var)]))
