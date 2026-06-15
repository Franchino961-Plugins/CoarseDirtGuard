# Changelog - CoarseDirtGuard

All notable changes to the **CoarseDirtGuard** plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.1.0] - 2026-02-01

### Added
- **Reload Command**: Dedicated `/coarsedirtguard reload` command (aliases: `/cdg`, `/cdguard`) — reloads configuration without `/reload confirm`. Requires `coarsedirtguard.reload` permission.
- **Info Command**: `/coarsedirtguard info` command — shows plugin status and active settings.
- **Granular Permissions**: New permission nodes: `coarsedirtguard.bypass`, `coarsedirtguard.bypass.shovel`, `coarsedirtguard.bypass.hoe`, `coarsedirtguard.reload`.
- **Debug Mode**: New `settings.debug` option in `config.yml`.

### Changed
- **Performance**: Replaced multiple comparisons with `EnumSet` for O(1) lookups in `isShovel()` and `isHoe()`.
- **Performance**: Used `HashSet<Material>` for protected blocks — faster lookups and Material validation on config load.
- **Documentation**: Full JavaDoc for all classes and methods.
- **Error Handling**: Advanced error handling — Material validation with fallback to `COARSE_DIRT`. Plugin disables itself on critical config errors.
- **Logging**: Structured logging with `String.format()`.

### Fixed
- **Stability**: Plugin no longer crashes on malformed config.
- **Validation**: Invalid Material entries are silently ignored with a warning.

---

## [1.0.0] - 2024-12-01

### Added
- **Initial Release**: First stable public release.
- **Block Protection**: Silently blocks dirt path transformations (shovels) and dirt transformations (hoes).
- **Tool Support**: Support for all vanilla shovel and hoe materials (wooden → netherite).
- **Configuration**: Customisable `config.yml` with per-tool toggles and optional logging.
- **Architecture**: Single Java class architecture for minimal performance footprint.
- **Events**: Event listener on `PlayerInteractEvent`.
- **Dependencies**: Zero runtime dependencies (Paper API provided only).

---

## Development Roadmap

### Phase 1 - Initial Release ✅
- Block protection system for shovels and hoes.
- Configurable per-tool toggles.

### Phase 2 - Commands & Permissions ✅
- Dedicated reload command.
- Granular bypass permissions.

### Phase 3 - Advanced Features 📋
- Per-world configuration.
- WorldGuard region support.
- Additional configurable materials.

---

*Format: [Version] - Date*
*Categories: Added, Changed, Fixed, Removed*
