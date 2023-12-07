# Rate Limiter 
**Ref: System desgin interview (Alex Xu, Chapter 4)**

- Use Redis for counting. Redis is high availability, in-memory computing, low latency and clustering (can support very large scale application).

## Fixed Window Counter Algorithm 
- For each fixed-window, assign a counter 
- Each request is equal and will increase by 1 when it called
- When counter reach to a threshold, reject the request until a new time-window start

Advantage:
- Memory efficient (only have key + 1 counter)
- Reset Quota for each window, fit in some scenarios 

Disadvantage: 
- Peak requests at edge window, can cause double quota in the same time-window length. For example, if requests peak at the second half of window 1, and at the first half of window 2. Then, for each window, quota rule is not violated. But for time span = second half window 1 + first half window 2, there are two quota times requests.
## Sliding Window Counter Algorithm 

When using sliding window approach, there will be overlap percentage of the rolling window and previous window (call previous overlap), the counter formula: 

`count = Requests in current window + requests in the previous window * overlap percentage of the rolling window and previous window`

Advantage:
- It smooths out spikes in traffic because the rate is based on the average rate of the previous window. 
- Memory efficient.

Disadvantage
- It only works for not-so-strict look back window.




