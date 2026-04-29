# Contributing to BookNM

First off, thank you for considering contributing to BookNM! It's people like you that make BookNM such a great tool for campus venue management.

## 1. Where do I go from here?

If you've noticed a bug or have a feature request, make one! It's generally best if you get confirmation of your bug or approval for your feature request before starting work.

## 2. Fork & create a branch

If this is something you think you can fix, then fork BookNM and create a branch with a descriptive name.

A good branch name would be (where issue #325 is the ticket you're working on):

```bash
git checkout -b 325-add-dark-mode
```

## 3. Implementation Guidelines

- Ensure your code follows the existing Java conventions.
- Keep the offline-first architecture intact. All data modifications should first happen locally via SQLite, and then sync to Firebase Firestore.
- Ensure that the GitHub Actions CI pipeline passes before submitting a pull request. The CI pipeline will automatically run `./gradlew build` on your PR.

## 4. Make a Pull Request

At this point, you should switch back to your master branch and make sure it's up to date with BookNM's master branch:

```bash
git remote add upstream https://github.com/aryan-arvind/BookNM.git
git checkout master
git pull upstream master
```

Then update your feature branch from your local copy of master, and push it!

```bash
git checkout 325-add-dark-mode
git rebase master
git push --set-upstream origin 325-add-dark-mode
```

Finally, go to GitHub and make a Pull Request.

## 5. Merging

Once your pull request is approved by a maintainer, it will be merged into the main branch.

Thank you for contributing!
