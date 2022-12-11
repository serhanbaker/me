# Matching Engine

This is a matching engine that takes in Limit Orders, and constructs an order book.

If the aggressor order (incoming order) matches with any existing resting orders, it will execute a trade and print an output.

Our data structure of choice is `TreeMap<Double, LinkedList<Order>>`, and orders are matched using the price-time priority strategy.

This means, we insert orders that don't have any match at the end of the linked list at the price level. This way, we guarantee that the first resting order we'll process will be the earliest inserted order at our matched price level (lowest ask price or highest bid price).

This structure allows for efficient insertion, retrieval, and removal of orders based on their price. The TreeMap provides a sorted structure for storing orders by their prices, and the LinkedList allows for quick insertion and removal of orders at a specific price level.

## **** ATTENTION ****
I didn't really understand the requested order book output template, so I implemented my own which resembles the industry standard price ladder representation of an order book.

For example, in the assignment document, this is the input:
```
10000,B,98,25500
10005,S,105,20000
10001,S,100,500
10002,S,100,10000
10003,B,99,50000
10004,S,103,100
```
And this is the requested output template:
```
50,000 99 | 100 500
25,500 98 | 100 10,000
      | 103 100
      | 105 20,000
```

It is not clear to me why we wouldn't aggregate the volumes of 10_000 and 500 on the price level of 100, or how we construct the output so that 99 and 100 will be at the same line, and 98 and 100 will be at the same line as well. Then we go from 99 to 98 (descending), and then 103 to 105 (ascending).

So, this is the output from my implementation for the same input:
```
Bid #		|		Price       |	Ask #
0		|	103.0             |     100
0		|	105.0             |     20000
0		|	100.0             |     10500
50000		|	99.0              |     0
25500		|	98.0              |     0
```
Which I think resembles a lot like the price ladder representation that's used widely in trading industry:

![Industry Standard Order Book](https://i.imgur.com/Dh7wYn0.jpg)

