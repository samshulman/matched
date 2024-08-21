# Matched_CS320

Final Group Project for CS320: Intro. to Software Engineering. Features: NLP (Natural Language Processing) analysis, Non-relational databases, vector/embedding similarity ranking algorithm

## About description from the webpage:

"Welcome to Matched.

Our goal is to increase connection among students at Brown. To form a new connection, all you have to do is specify your match type (study buddy, date, or friend), then fill out a quick questionnaire. We then use a natural language processing model, Cohere, to score the similarity between your answers to the questionnaire and others' answers. Our algorithm takes into account the similarity between each question equally and returns your top 3 matches!

When using Matched, keep in mind that meeting new people based on similarity isn't the only way. We encourage you to find many ways to meet new people, including ways that promote connections with people who have different interests than you do."

### My contribution:

  I worked on the backend with one other person in our group. Specifically, I implemented the database, including the SDK functionality and the rest API functionality provided by Google Firebase. I also designed the structure of the database to be flexible enough to easily update the matches page in the front end without the need for caching. I also worked on the get-matches algorithm with the other person on the backend. Finally, I tested the backend and helped debug and solve a lot of issues before our final code demo, including errors on the front-end with logging in, reconnected the frontend to go through our backend API rather than connect directly to the database, and last but not least, cleaning up the code to fix edge cases causing infinite loops and otherwise undesirable behavior.
  
  Given more time to work on this project, implementing a cache where users’ matches are stored in a cache would improve efficiency. Additionally, some functionality provided on the backend, such as being able to delete your profile as well as update it, was not implemented on the front end due to time constraints. Additionally one of our goals was to accomplish the Strategy Pattern, which in our case was to allow developers to easily add new forms to be completed. This was mostly completed, but given time constraints was not fully polished. Our group all agreed that if we were to continue working on this, extending that strategy pattern to allow for user-created forms would yield the greatest results in terms of the purpose of the project.

