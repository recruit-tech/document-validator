[![Build Status](https://travis-ci.org/redpen-cc/redpen.svg?branch=master)](https://travis-ci.org/redpen-cc/redpen)
[![Coverage Status](https://coveralls.io/repos/recruit-tech/redpen/badge.png)](https://coveralls.io/r/recruit-tech/redpen)
[![Build status](https://ci.appveyor.com/api/projects/status/qtqmphigaip8lfwk?svg=true)](https://ci.appveyor.com/project/takahi-i/redpen)
[![Gitter chat](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/recruit-tech/redpen)

Overview
=========

RedPen is a proofreading tool to help writers or programmers who write technical documents or manuals that need to adhere to a writing standard. RedPen automates the verifications of input documents written in natural languages


<p align="center">
   <a href="http://redpen.cc"><img src="logo/redpen-logo.png"/></a>
</p>

See the detail usage in the RedPen [home page](http://redpen.cc/).

Features
=========

- Language independent
- Highly customizable
- Multi platform (MacOS, Windows, and Linux)
- Support various markup text formats (Wiki, AsciiDoc, LaTeX)
- [Plugin system: users can write their extensions with JavaScript](http://blog.redpen.cc/2015/09/08/writing-extension-with-javascript/)
- [Practical REST API and UI](http://redpen.herokuapp.com/)
<p align="center">
   <a href="http://redpen.herokuapp.com/"><img src="logo/redpen-ui.png"/></a>
</p>


Ecosystem
==========

Tools
-----

  * [Docker Image by Naoki Ainoya](https://registry.hub.docker.com/u/ainoya/redpen-server/)
  * [RubyGem by akinomurasame](https://rubygems.org/gems/redpen_ruby)
  * [Python Client](https://pypi.python.org/pypi/pyredpen/)
  * [Android App by Shintaro Katafuchi](https://play.google.com/store/apps/details?id=cc.redpen)

Editor support
---------------

  * Atom
    * [redpen package by griffin-stewie](https://atom.io/packages/redpen)
    * [linter-redpen by griffin-stewie](https://atom.io/packages/linter-redpen)
  * [Emacs redpen-paragraph by karronoli](https://libraries.io/emacs/redpen-paragraph/v0.2)
  * [WordPress plugin](https://github.com/redpen-cc/redpen-wordpress-plugin)
  * [IntelliJ IDEA plugin](https://plugins.jetbrains.com/plugin/8210)
  * [SublimeLinter module for RedPen](https://github.com/taky/sublimelinter-redpen)

Extensions
----------
  * [unsuggested-kanji](https://github.com/so-c/redpen-validator) by so-c
  * [ra-removed-word](http://www.clear-code.com/blog/2015/8/29.html) by ClearCode

How to contribute
==================

We welcome any contributions through Github pull requests. When you make changes such as adding new features and the tests,
please run test before throw the pull reqest. You can run test with the mvn test command.

Deploy
======
[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

Links
======

* [Mailing List](https://groups.google.com/forum/#!forum/redpen-validator)
* [Docs](http://redpen.cc/docs.html)
* [JavaDoc](http://redpen.cc/javadoc/latest/index.html)
* [Blog](http://blog.redpen.cc)
