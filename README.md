# Matching Engine
This project is an implementation of a unique asset trade system. This implementation 
supports fixed price orders and trade market price. 
The complexity of any operation in the system is at least $O(n)$, look at [Complexity analysis](#complexity-analysis) to see more.


## How to use it
Compile the java files in *src/main* and execute the Main file. 

The system supports the following operations: 

**print book** - show the current buy and sell orders in the book.

**cancel** *order_id* - remove the order identify by the *order_id* identifier.

To create a new order, use the syntax:

* market order - *market side qty*
* Limit order - *limit side price qty* || *limit <bid|offer> side qty* || *limit <bid|offer> side price qty
* Peg order - *peg <bid|offer> side qty*

let us look at an example:
    
    java Main 
    > limit buy 143.22 150 
    Order created: BUY 150 @ 143.22 Order@7f31245a
    > peg bid 100 
    Order created: BUY 100 @ 143.22 Order@7h55345b
    > market sell 200 
    Trade: price 143.22, quantity: 150
    Trade: price 143.22, quantity: 50
    > exit 

## Complexity analysis
### Order
The Order class only has operations with complexity $O(1)$.  

### Node 
The Node class only has operations with complexity $O(1)$ 

### LinkedList 
The LinkedList class only has operation with complexity $O(1)$ 

This implementation of a Linked List has two important points:

**The LinkedList is FIFO** 

**The remove operation is $O(1)$**

The first point is possible using a head and tail node, and the second is possible
using node reference to remove. 

### Book  
Let us start studying the complexity of the book with the case of non-peg orders.    
Since book implementation is based on TreeMap using a *double* as key and a *LinkedList* of *Orders* as value,
so operations such as add and remove in the TreeMap have complexity $O(\lg n)$.

But suppose now that there is a huge number of peg orders in the book, so with a new bid or 
a new offer, there is a huge number of orders to be updated in the tree.

**Edge Case Consideration**:
If there are many peg orders, the complexity of adding/removing orders could 
increase since multiple orders need to be updated across the tree, leading to a complexity of O(n).
Another edge case is when we need a huge number of partial trades, since for each trade the
complexity is $O(\lg n)$ this can be time-consuming.

The Book class is implemented supposing that this kind of edge case is improbable. 
So the other's operations can be faster 

## Tests 
There is a python file named CostumerSimulator in the test module that 
simulates buy and sell orders on the market, generating a possible input. 

The idea is to simulate a market where the order price is stable, so build a normal distribution to buy values and another for sale values. 
Where we have limit sell price > limit buy price normally. Simulating the buyer needs 
for lower sell price, and the seller needs for a higher buy price. 

The market orders simulate the immediate for a buy or a sell. So a huge number of limit 
trades are less probable. 

Notice that this simulation model has no scientific basis, just a guess.


Thanks, enjoy!

