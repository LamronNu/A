# Auction

This project realizes primitive auction system, that provide:
+ register of user
+ login user to the system (only authorized users can work with it)
+ user can:
  + create new lot
  + edit lot (only when it is in active state and user is its owner)
  + cancel trades (the same condition)
  + create new bid to lot (when user is not its owner and bid is more than other lot`s bids and lot start price)
+ actions with lots:
  + show all lots
  + only active lots (state = "Active")
  + only user's lots (where current user is owner)
  + only actual lots (where user can make a bid, e.g. lot is active and user is not owner)
  + refresh lots (check data from db)
+ every 30 min system refresh data (by shedule)

<i>More details</i> are in <a href="https://github.com/LamronNu/A/blob/master/TestTask.pdf">pdf-file</a>.

<b>Used techologies:</b>

IDE:
+ Inteliji IDEA

Back:
+ Web-Services JAX-WS
+ JDBC
+ Log4j
+ Maven
+ Quarts

GUI:
+ Vaadin framework

Databases:
- dev: MySQL
- production: Postgres

result app: https://auction-example.herokuapp.com/
