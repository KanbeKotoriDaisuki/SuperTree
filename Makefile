default: compile test run

compile: \
	Backend.java \
	BackendInterface.java \
	BackEndDeveloperTests.java \
	Color.java \
	CreateFiles.java \
	DataWranglerTests.java \
	DSLanguage.java \
	Frontend.java \
	FrontendException.java \
	Hero.java \
	Main.java \
	namedHero.java \
	RedBlackTree.java \
	SortedCollectionInterface.java \
	SuperheroDataReader.java \
	SuperheroDataReaderInterface.java \
	SuperheroInterface.java \
	TestFrontend.java \
	Viewport.java 

	javac CreateFiles.java && javac Main.java

run: compile
	. ./run.sh

test: compile junit5.jar 
	javac -cp .:junit5.jar DataWranglerTests.java
	javac -cp .:junit5.jar BackEndDeveloperTests.java
	javac -cp .:junit5.jar TestFrontend.java

clear:
	rm *.class