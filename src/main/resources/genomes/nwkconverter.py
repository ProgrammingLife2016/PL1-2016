import re, sys

file = "340tree.rooted.TKK.nwk" if len(sys.argv) == 1 else sys.argv[1] 
f = open(file, "r").read()

def convert(m):
    a = float(m.group(1))
    b = float(m.group(2))
    res = a * 10 ** b
    print "{} = {:.17f}".format(m.group(0), res)
    return "{:.17f}".format(res)

res = re.sub("(\d\.\d+)e((-|\+)\d{2})", convert, f)
w = open("n340.nwk", "w")
w.write(res)
w.close()
