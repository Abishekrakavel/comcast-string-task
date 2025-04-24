Please make notes here to clarify any decisions taken that you wish to communicate.

1) My Doubt after initially going through the code base given: Why do we have get and post call which access the same method, basically get is idempotment and post is not. And in get call we won't change the state but here we are adding data to the map in get call. But in post only we usually do.
My Thoughts: The get should only check whether the string is in the map or not, it shouldn't put the data into the map. I was having a doubt on this, but in order to work with the problem statement I haven't focused on that part.

2) Taking the decision by myself that, this might need a support for multi threaded environment, so I have used in memory cache implemented using ConcurrentHashMap for storing the strings.

3) To persist data even after shutting down the server, I used a simple Json file which was my design decision. In real world I would have used Redis cache.

4) Have used resilience 4j rate limitter to achieve the rate limiting by creating a custom rate limitter for each client IP. But resilience 4j doesnot have time to live functionality
Thought of creating a wrapper class to manually that custom created rate limiter into map and delete it at when the size of the map reaches certain limit or else could have implemented TTL or LRU logic.
Since this is a simple application, and we are not hosting it so just created a custom rate limiter for the IP and not handled the delete case.


