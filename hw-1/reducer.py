import sys
import math

SEP = '|'
mean, var, count = 0, 0, 0

for line in sys.stdin:
    line = line.strip()
    _, ck, mk, vk = line.split(SEP, 3)
    try:
        ck = int(ck)
        mk = float(mk)
        vk = float(vk)
    except:
        continue
    mean = (mean * count + mk * ck) / (count + ck)
    var = ((var * count + vk * ck) / (count + ck) + count * ck * math.pow((mean - mk) / (count + ck), 2))
    count += ck

print(count, mean, var)
