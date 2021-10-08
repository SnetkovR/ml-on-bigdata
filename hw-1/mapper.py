import sys

SEP = ','
OUT_SEP = '|'
TARGET_COLUMN = -7
_sum, square, _count = 0, 0, 0

for line in sys.stdin:
    line = line.strip()
    try:
        price = float(line.split(SEP)[TARGET_COLUMN])
    except IndexError:
        continue
    except ValueError:
        continue
    _sum += price
    square += price * price
    _count += 1

chunk_mean = _sum / _count
chunk_var = square / _count - chunk_mean * chunk_mean

# В целом, ключ добавлять не обязатально, т.к. у нас 1 редьюсер
print(OUT_SEP.join([str(_count), str(chunk_mean), str(chunk_var)]))
