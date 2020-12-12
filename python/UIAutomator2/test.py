#

def a(x, y):
    print(str(x+y))
try:
    a(1, 2)
except BaseException:
    print("error")