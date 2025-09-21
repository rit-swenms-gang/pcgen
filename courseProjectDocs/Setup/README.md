### Setup
Setup is pretty easy. The steps are listed in the original project readme but with some extraneous steps so here's the steps I followed:

1. Install PCGen from the installer. It's labeled labled 6.09.xx. on here: https://github.com/PCGen/pcgen/releases/ 
2. Make sure you've got an up-to-date Java and git
3. Clone the project
4. Run the following two commands
  git remote add upstream https://github.com/PCGen/pcgen
  git checkout master && git fetch upstream && git rebase master
5. You should be good!!

### Testing
Built-in testing is done through Gradle and Jacoco. I ran the following two commands to generate the test reports in this folder:
./gradlew test
./gradlew jacocotestreport

### Test Results
My god, there are a lot of tests for this application. I guess I am not used to working hands-on with software at this scale, but geez, it's a lot. 16,741 tests were run, with some command-line test stuff being ignored. All of them were successful and was completed in under a minute, so didn't seem to be any efficiency problems. Test coverage is kind of across the board with some packages being at 100% and some packages at 0 and everything inbetween. It seems like the functional parts of the application, the ones you actually want to test, are largely at least 70% covered or better.  

