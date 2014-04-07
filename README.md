# ttc14-movie-couples

This is the [FunnyQT](http://jgralab.github.io/funnyqt/) solution to the Movie
Database Case of the
[Transformation Tool Contest 2014](http://www.transformation-tool-contest.eu/).

## Usage

The transformation can be run simply by calling `lein test` from the command
line.  It'll then run both tests benchmarking with the synthetic models and
tests benchmarking with the IMDb models.

In both cases, evaluation times and computed numbers are printed.  The results
of the top-15 queries are spit into files in the `query-results/` directory.

To only run the benchmarks for the synthetic or IMDb models, the transformation
may be called with an additional parameter, e.g. `lein run :synthetic` or `lein
run :imdb`.

## License

Copyright © 2014 Tassilo Horn <horn@uni-koblenz.de>

Distributed under the GNU General Public License, version 3 (or later).
