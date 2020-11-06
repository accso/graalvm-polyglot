def fib(n):
    acc = [0, 1]
    for i in range(0, n):
        first = acc.pop(0)
        acc.append(acc[0] + first)
    return acc[0]

def short(n):
    if n > 100000000:
        return "..."
    else:
        return str(n)

def test_fib(n):
    print('fib({}): {}'.format(n, short(fib(n))))

test_fib(0)
test_fib(1)
test_fib(2)
test_fib(5)
test_fib(35)
test_fib(1000)
test_fib(500000)
