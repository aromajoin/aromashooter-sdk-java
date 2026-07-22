# Contributing to the Aroma Shooter Java SDK

Thanks for taking the time to help improve the SDK.

## What lives in this repository

This repository holds the **samples and documentation** only. The library itself is
published to Maven Central as `com.aromajoin.sdk:jvm` and `com.aromajoin.sdk:core`,
and is developed in a separate repository maintained by Aromajoin.

That means:

- Pull requests are welcome for the [Sample project](Sample), the README, and anything
  else in this repository.
- Bugs in the library cannot be fixed by a pull request here, but they are still tracked
  here — please open an issue and we will carry the fix into the library.

## Before reporting a bug

Two behaviours get reported often and are not bugs. Please rule them out first:

- **Nothing comes out, but there is no error.** The internal booster has to be on for
  scent to be emitted. Pass `internalBooster: true` for the simple API, or an
  `internalBoosterIntensity` above 0 for the intensity API.
- **The scent does not stop.** The simple and intensity APIs send different commands and
  each has its own stop. `stopAllChambers()` will not stop a shoot started with
  `shootWithIntensity*` — use `stopAllChambersWithIntensity()`.

The [Troubleshooting section](README.md#troubleshooting) covers the rest.

## Reporting a bug

Search the [open issues](https://github.com/aromajoin/aromashooter-sdk-java/issues) first
to check it has not already been reported. If it has not,
[open a new one](https://github.com/aromajoin/aromashooter-sdk-java/issues/new) with a
clear title and description, plus:

- SDK version, JDK version and operating system
- Aroma Shooter model (AS1, AS2, AS3) and its serial
- What you expected to happen and what happened instead
- A minimal code sample or test case that reproduces it

Device behaviour depends on the model and firmware, so the model and serial matter more
than they usually would.

GitHub's own guide on [creating an issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/using-issues/creating-an-issue)
is a good reference if you have not filed one before.

## Pull requests

- Keep the change focused on one thing.
- Match the surrounding style; the samples target JRE 1.8.
- If the change affects how the SDK is used, update `README.md` **and** `README-JP.md` —
  the two are kept in step.

## Code of conduct

Participation is covered by our [Code of Conduct](CODE_OF_CONDUCT.md).
