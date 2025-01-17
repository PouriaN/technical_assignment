# Read Me First

The following was discovered as part of building this project:

* The JVM level was changed from '23' to '21' as the Kotlin version does not support Java 23 yet.

# Getting Started

For running the project you must download the following files from IMDB dataset and put them in the project path
* name.basics.tsv
* title.basics.tsv
* title.crew.tsv
* title.principals.tsv
* title.ratings.tsv

#### note: please increase the maximum heap size to 12G 

# Test
you can test the functionality using the following URIs
* http://localhost:8080/titles/same_director_writer?pageSize=20&pageNumber=1
* http://localhost:8080/titles/actors?actors=nm0443482&actors=nm0653042
* http://localhost:8080/titles/genre/Animation
