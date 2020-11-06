def fib(n):
    if n == 0: 
        return 0
    if n == 1:
        return 1
    return fib(n-1) + fib(n-2)

def test_fib(n):                                                                                                                                print('fib({}): {}'.format(n, fib(n)))

test_fib(0)
test_fib(1)
test_fib(2)
test_fib(5)
test_fib(35)
