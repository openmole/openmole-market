READ ME

## WHAT IS IT?

This is an implementation of a prisonner’s dilemma metamimetic game for different network topologies under netlogo 5.1.0. 


Agents play the prisoner's dilemma game with each one of their neighbours in a torus lattice or a network (small world or random).   

The parameter p corresponds to the strength of dilemma in the widget:



                                 Payoff Matrix
                                 -------------
                                    OPPONENT
          BEHAVIORS   Cooperate            Defect
                        -----------------------------
           Cooperate |(1-p, 1-p)            (0, 1)
      YOU            |
           Defect    |(1, 0)                (p, p)
    
            (x, y) = x: your score, y: your partner's score
            Note: higher the score (amount of the benefit), the better.


The agents can have one of 4 types:

Maxi : The agent tries to maximize the score (payoff)  
Mini : The agent tries to minimize the score  
Conformist: The agent tries to behave as the majority   
Anti-conformist: The agent tries to behave as the minority

Each round agents play they copy the most succesfull agent in their neighborhood according to their own current type: 

A Maxi agent would copy the type and behavior (C or D) of the agent in its neighborhood with highest payoffs. 
A Mini agent would copy the type and behavior (C or D) of the agent in its neighborhood with lower payoffs.
A Conformist would copy the type and behavior (C or D) that the majority of its neighborhood is using. 
An Anti-Conformist would copy the type and behavior (C or D) that the minority of its neighborhood is using.

   
## HOW TO USE IT

Decide the topology structure or load one.
Decide a Number of Agents that will be playing. 
Decide what percentage of agents should cooperate at the initial stage with inicoop.
Decide what is the strenght of the dilemma p.
If you are not loading a topology; choose the parameters for the desired topology. 
Choose to add noise to the model by renovating the population and have some agents die. 

## HOW IT WORKS

At each period: 

Each agent A plays a prisoner's dilemma game pairwise with all of its neighbours. The scores for all the pairwise games played are summed up to become the new payoffs of the agent.  

Each agent looks at the payoffs, decision-making rules and behaviours of other agents in its neighbourhood Gamma_A. 

For any agent A, if according to A's  type (payoffs based or non-materialistic) there is one neighbour B that is more successful than A himself, and if B has a different decision-making rule, then A copies the rule of agent B. In the case of two or more candidates to copy, then A chooses one of the rules at random. 

If according to its new rule of behaviour and its associated utility function, A is still not among the most successful agents in the neighborhood, then A copies the behaviour of the neighbour with the best situation.


## EXAMPLE:

 If agent A had the conformist type and if the majority of its neighbours have turned to maxi since last round, then A will adopt the maxi rule. Here, the imitation rule is used to update itself (reflexivity).
 If same agent A, which is now a maxi agent, played C last round but a D-player did strictly better than all of A’s neighbours (A included), then A will become a D-player. Here, the imitation rule is used to update the behaviour (metacognition).


## THINGS TO NOTICE
How do populations change with the strength of the dilemma?
What is the effect of noise with renovation of the population?
Where are agents located in the network at the attractor? 
What is the effect of the initial disposition towards cooperation? 
Are there differences in the final populations for each topology?

