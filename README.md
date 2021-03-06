Warnings
========

1. Don't delete `default.properties` in the eclipse project folder.

Tips for Setup the Projects
===========================

Git
---

### Clone the Repo to Local
    git clone git@github.com:telgniw/BukuDroid.git

### Commit Local Changes
    git add files_to_be_commited
    git commit -m "Comment for this commit."

### Push Local Commits to Repo
    git push origin master

Eclipse
-------

### Import Eclipse Project
`File` -> `Import` -> `Existing Projects into Workspace` -> Select a Folder

### Clean Eclipse Project
`Project` -> `Clean` -> `Clean all projects.`

### Setup Project Dependency
1. Import all projects under the folder `/Mobile`.
2. Right-Click on the Projects -> `Preferences` -> `Android`: Ensure `Is Library` is checked.
  - `ActionBar`
  - `FacebookAPI`
  - `ViewPagerIndicator`
  - `ZXing`
3. `BukuDroid` -> `Preferences` -> `Android`: Add the above Projects to `Library`.
4. Clean all projects if still having errors.
