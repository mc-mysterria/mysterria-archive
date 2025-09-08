# Mysterria Archive

A collaborative REST API for cataloging and discussing custom items from the Lord of The Mysteries world on a Minecraft server.

## Overview

The Mysterria Archive allows players worldwide to share discovered custom items, magical ingredients, artifacts, mobs, and structures. Other players can browse the archive, search for specific items, and contribute their knowledge through comments.

## Features

- **Item Management**: Create, read, update, and delete custom items
- **Advanced Filtering**: Filter by pathways, types, sequences, and rarity levels
- **Comment System**: Collaborative knowledge sharing through comments
- **Researcher Profiles**: Simple nickname-based identification
- **Search Functionality**: Full-text search across item names and descriptions
- **LOTM Integration**: Support for pathways (Sun, Fool, Error, etc.) and sequence system (0-9)

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **MariaDB**
- **Lombok**
- **Jakarta Validation**
- **Gradle**

## Quick Start

### Prerequisites

- Java 21 or higher
- MariaDB/MySQL database
- Gradle (or use included wrapper)

### Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd MysterriaArchive
```

2. Configure database in `application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/mysterria_archive
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build and run:
```bash
./gradlew clean build
./gradlew bootRun
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Items
- `GET /api/items` - List all items (supports `?search=` parameter)
- `GET /api/items/{id}` - Get specific item
- `POST /api/items` - Create new item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item
- `GET /api/items/researcher/{id}` - Items by researcher
- `GET /api/items/pathway/{id}` - Items by pathway
- `GET /api/items/type/{id}` - Items by type
- `GET /api/items/sequence/{number}` - Items by sequence (0-9)
- `GET /api/items/rarity/{rarity}` - Items by rarity

### Pathways
- `GET /api/pathways` - List all pathways
- `GET /api/pathways/{id}` - Get pathway details
- `POST /api/pathways` - Create pathway
- `PUT /api/pathways/{id}` - Update pathway
- `DELETE /api/pathways/{id}` - Delete pathway

### Types
- `GET /api/types` - List all item types
- `GET /api/types/{id}` - Get type details
- `POST /api/types` - Create type
- `PUT /api/types/{id}` - Update type
- `DELETE /api/types/{id}` - Delete type

### Comments
- `GET /api/comments/item/{itemId}` - Get comments for item
- `POST /api/comments` - Add comment
- `DELETE /api/comments/{id}` - Delete comment

### Researchers
- `GET /api/researchers` - List all researchers
- `GET /api/researchers/{id}` - Get researcher details
- `POST /api/researchers` - Create researcher
- `PUT /api/researchers/{id}` - Update researcher
- `DELETE /api/researchers/{id}` - Delete researcher

## Data Models

## Validation Rules

### Items
- Name: 2-100 characters, required
- Description: Optional, unlimited length
- Purpose: Optional, unlimited length
- Researcher ID: Required
- Pathway ID: Optional
- Type ID: Optional
- Sequence Number: 0-9, optional
- Rarity: COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHICAL

### Pathways
- Name: 2-50 characters, required, unique
- Description: Optional
- Sequence Count: 1-9, required

### Types
- Name: 2-50 characters, required, unique
- Description: Optional
- Icon URL: Optional, valid URL format

### Comments
- Content: 1-2000 characters, required
- Item ID: Required (must exist)
- Researcher ID: Required (must exist)

### Researchers
- Nickname: 3-30 characters, required, unique

## Database Schema

The application uses 5 main entities:
- **ArchiveItem**: Core item data with relationships
- **ArchivePathway**: LOTM pathway classification
- **ArchiveType**: Item categorization
- **ArchiveComment**: User comments on items
- **ArchiveResearcher**: User identification

## Development

### Build Commands
```bash
# Clean build
./gradlew clean build

# Run tests
./gradlew test

# Run without tests
./gradlew clean build -x test

# Start application
./gradlew bootRun
```

## License

This project is for educational and gaming community purposes.

## Lord of The Mysteries Theme

This archive is themed around the Lord of The Mysteries webnovel world, featuring:
- **Pathways**: Mystical paths of power (Sun, Fool, Error, etc.)
- **Sequences**: Power levels within pathways (0-9)
- **Rarity System**: From Common to Mythical items
- **Item Types**: Ingredients, Artifacts, Mobs, Structures
- **Collaborative Research**: Players sharing mystical knowledge