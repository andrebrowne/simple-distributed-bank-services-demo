# Contributing to simple-distributed-bank-services-demo

We welcome contributions from the community and first want to thank you for taking the time to contribute!

Please familiarize yourself with the [Code of Conduct](https://github.com/vmware/.github/blob/main/CODE_OF_CONDUCT.md) before contributing.

Before you start working with simple-distributed-bank-services-demo, please read our [Developer Certificate of Origin](https://cla.vmware.com/dco). All contributions to this repository must be signed as described on that page. Your signature certifies that you wrote the patch or have the right to pass it on as an open-source patch.

## Ways to contribute

We welcome many different types of contributions and not all of them need a Pull request. Contributions may include:

* New features and proposals
* Documentation
* Bug fixes
* Issue Triage
* Answering questions and giving feedback
* Helping to onboard new contributors
* Other related activities

## Getting started

This project requires git, Java 17 (or higher) and gradle.

This project is configured as a single gradle project with sub-projects containing 3 separate Spring Boot Applications (`account-service`, `audit-serice`, and `debit-serice`) . 

### Build
To build all 3 applications at once execute:
```shell
./gradlew clean build
```

To build a single the application (.e.g. `account-service`) execute:
```shell
./gradlew :account-service:clean :account-service:build
```
### Run
Each Spring Boot Application's `local` profile propoerties are configured to allow the 3 applications to comunicate with each other on ports `8081`, `8082`, and 
`8083` on a host machine. Running all the applications, at once, can be achieved simply by executing:
```shell
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun    
```

However, running them separately is also an option.
To run the `account-service` open a new terminal and execute:
```shell
SPRING_PROFILES_ACTIVE=local ./gradlew :account-service:bootRun
```
To run the `audit-service` open a new terminal and execute:
```shell
SPRING_PROFILES_ACTIVE=local ./gradlew :audit-service:bootRun
```
To run the `debit-service` open a new terminal and execute:
```shell
SPRING_PROFILES_ACTIVE=local ./gradlew :debit-service:bootRun
```

### Test
Each application has a test suite. Execute the following to run the tests for all 3 applications at once:
```shell
./gradlew test
```

To run the tests for a single application (.e.g. `audit-service`) execute:
```shell
./gradlew :audit-service:test
```

## Contribution Flow

This is a rough outline of what a contributor's workflow looks like:

* Make a fork of the repository within your GitHub account
* Create a topic branch in your fork from where you want to base your work
* Make commits of logical units
* Make sure your commit messages are with the proper format, quality and descriptiveness (see below)
* Push your changes to the topic branch in your fork
* Create a pull request containing that commit

We follow the GitHub workflow and you can find more details on the [GitHub flow documentation](https://docs.github.com/en/get-started/quickstart/github-flow).

### Pull Request Checklist

Before submitting your pull request, we advise you to use the following:

1. Check if your code changes will pass both code linting checks and unit tests.
2. Ensure your commit messages are descriptive. We follow the conventions on [How to Write a Git Commit Message](http://chris.beams.io/posts/git-commit/). Be sure to include any related GitHub issue references in the commit message. See [GFM syntax](https://guides.github.com/features/mastering-markdown/#GitHub-flavored-markdown) for referencing issues and commits.
3. Check the commits and commits messages and ensure they are free from typos.

## Reporting Bugs and Creating Issues

For specifics on what to include in your report, please follow the guidelines in the issue and pull request templates when available.

## Ask for Help

The best way to reach us with a question when contributing is to ask on:

* The original GitHub issue
* The developer mailing list
* Our Slack channel
