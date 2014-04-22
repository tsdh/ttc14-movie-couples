# ttc14-movie-couples

This is the [FunnyQT](http://jgralab.github.io/funnyqt/) solution to the Movie
Database Case of the
[Transformation Tool Contest 2014](http://www.transformation-tool-contest.eu/).

## Usage

### Running on SHARE

If you want to run the solution on the SHARE image, first make sure that the
`~/Desktop/sharedfiles/` folder is mounted (non-empty).  If it is not, then
mount it using the `mount-sharedfiles` script in `~/bin/`.

### Running the tests/benchmarks

The transformation can be run simply by calling `lein test` from the command
line.  It'll then run both tests benchmarking with the synthetic models and
tests benchmarking with the IMDb models.

In both cases, evaluation times and computed numbers are printed.  The results
of the top-15 queries are spit into files in the `query-results/` directory.

To only run the benchmarks for the synthetic or IMDb models, the transformation
may be called with an additional parameter, e.g. `lein test :synthetic` or
`lein test :imdb`.

Since `lein test :imdb` will run both the transformation creating couples and
cliques of three (which takes much longer) for each IMDB model, you can also
run only one of both tests using `lein test :only
ttc14-movie-couples.core-test/test-imdb-models-couples` or `lein test :only
ttc14-movie-couples.core-test/test-imdb-models-triples`.

## License

Copyright © 2014 Tassilo Horn <horn@uni-koblenz.de>

Distributed under the GNU General Public License, version 3 (or later).
