# Nightmare Defenders

_Nightmare Defenders_ is a narrative-rich two-player video game based around three primary design goals:
 * Use narrative as the primary focus of gameplay
 * Incorporate modern technology into the narrative-rich genre
 * Encourage cultural literacy

This game is being developed by a multidisciplinary team of
undergraduate students at [Ball State University](http://bsu.edu)
under the mentorship of [Paul Gestwicki](http://www.cs.bsu.edu/~pvg).

## Technology
This project uses [PlayN](http://playn.io), including the
[TriplePlay](https://github.com/threerings/tripleplay) library,
with primary focus on the HTML target.
The project is configured for development using IntelliJ IDEA Community Edition.

### Continuous Integration
The latest build of the master branch can be found at [http://spring-studio-2016.github.io/monsters](http://spring-studio-2016.github.io/monsters).

### Narrative Override
The default narrative is supplied by the asset file `narrative.json`.
However, replacement narratives can be supplied at runtime on the HTML
build by passing the "override" parameter in the http request. For example,
when running the HTML build locally, use the url `http://127.0.0.1?override`
to access this feature. Note that this only replaces the narrative file
and does not manipulate known regions or loaded image assets, so proceed
with caution.
