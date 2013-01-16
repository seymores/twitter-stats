# twitter-stats

Twitter statistic tool in Clojure.

# "Fake" Rules Spec

## Overview
Specify the rules to determine the likelihood of a twitter account is fake. There is not absolute certaintly that an account is fake, just the scoring probability that it is fake.

---

## Basic Score
Check the basic user profile to determine if this is a thowaway account or account that the user never bother to customized. The idea is that fake account user don't spend time customizing the account and leave as much as possible to default settings.

- Follower/Friends ratio - balance ratio is best
- In any block list?
- In any spam list?
- Has profile picture? Non-default is better
- Has customized wallpaper or background? Non-default is better
- Is the account location set?
- Is the user a verified user? --> Clearest indication that this account is atleast active and being invested on


## Conversation Score
Check if the user is engaging and if anybody is having conversation with this account.

- Reply to tweets
- Favorite any tweets
- Mentions by others
- Retweet vs original tweet
- Sign-up date / number of tweets
- Sign-up created-at date - older better

## Network Score
Check if the account is being followed or following any other fake accounts. Genuine account will have genuine followers and genuine friends.

- Followers mostly fakes?
- Follower / Friends ratio
- Friend accounts mostly legit?
- Follower accounts mostly legit?

---

## Usage

(require '[twitter-stats.core])

## License

Copyright © 2013 
Distributed under the Eclipse Public License, the same as Clojure.

