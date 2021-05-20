.DEFAULT_TARGET = compile

REBAR := rebar3

all: clean compile dialyzer test

# Compiles the whole application
compile:
	$(REBAR) compile
	$(REBAR) xref | grep -v "is unused export (Xref)"

# Cleans any generated files from the repo (except dependencies)
clean:
	$(REBAR) clean
	rm -f test/ebin/*
	rm -f doc/*.html doc/*.css doc/*.png doc/edoc-info

# Runs every test suite under test/ abd generates an html page with detailed info about test coverage
test: compile
	$(REBAR) eunit

# Generates the edoc documentation and places it under doc/ .
docs:
	$(REBAR) doc

# Launches an erlang shell where the deps and the modules from the project are accesible
shell: compile
	$(REBAR) shell

getdeps:
	$(REBAR) get-deps

dialyzer:
	dialyzer ebin -o  .eunit/dialyzer.log
