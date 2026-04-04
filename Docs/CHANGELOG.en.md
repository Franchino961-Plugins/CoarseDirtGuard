# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - February 2026 — Optimisations and New Features

### Performance
- `EnumSet` O(1) lookup: replaced multiple comparisons with `EnumSet` for O(1) lookups
  - `isShovel()` and `isHoe()` now use `EnumSet.contains()` instead of OR chains
- `HashSet<Material>` for protected blocks: faster lookups and Material validation on config load

### Added
- Dedicated `/coarsedirtguard reload` command (aliases: `/cdg`, `/cdguard`)
  - Reloads configuration without `/reload confirm`
  - Requires `coarsedirtguard.reload` permission
- `/coarsedirtguard info` command — shows plugin status and active settings
- Granular permissions system:
  - `coarsedirtguard.bypass` — bypasses all protections
  - `coarsedirtguard.bypass.shovel` — bypasses shovel restrictions only
  - `coarsedirtguard.bypass.hoe` — bypasses hoe restrictions only
  - `coarsedirtguard.reload` — allows configuration reload
- Debug mode: new `settings.debug` option in `config.yml`

### Improved
- Full JavaDoc for all classes and methods
- Advanced error handling: Material validation with fallback to `COARSE_DIRT`
- Plugin disables itself on critical config errors
- Structured logging with `String.format()`

### Fixed
- Plugin no longer crashes on malformed config
- Invalid Material entries are silently ignored with a warning

## [1.0.0] - December 2024 — Initial Release

### Added
- First stable public release
- Block protection system: silently blocks dirt path transformations (shovels) and dirt transformations (hoes)
- Support for all vanilla shovel and hoe materials (wooden → netherite)
- Customisable `config.yml` with per-tool toggles and optional logging
- Single Java class architecture for minimal performance footprint
- Event listener on `PlayerInteractEvent`
- Zero runtime dependencies (Paper API provided only)
- MIT License

## [Unreleased]

### Planned
- Per-world configuration
- WorldGuard region support
- Additional configurable materials

---

## Version History

### How to Read Version Numbers
- **Major.Minor.Patch** (e.g., 1.1.0)
  - **Major**: Breaking changes or major feature additions
  - **Minor**: New features, backward compatible
  - **Patch**: Bug fixes and small improvements

[1.1.0]: https://github.com/Fagghino/CoarseDirtGuard/releases/tag/v1.1.0
[1.0.0]: https://github.com/Fagghino/CoarseDirtGuard/releases/tag/v1.0.0
