# Assembler Simulator for Machine Code Conversion

This project is an **Assembler Simulator** that converts assembly code into machine code. The simulator reads assembly instructions, translates them into binary, and then generates corresponding decimal codes. This tool helps in understanding how assembly instructions are processed into machine code.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Directory Structure](#directory-structure)
- [Getting Started](#getting-started)
- [Usage Instructions](#usage-instructions)
- [Example](#example)
- [Dependencies](#dependencies)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

The Assembler Simulator is designed to read an input assembly file, convert it into machine code (both in binary and decimal format), and then write the result into output files. This project supports various types of instructions including **R-Type**, **I-Type**, **J-Type**, **O-Type**, and **Fill-Type**.

### Supported Assembly Instructions
- **R-Type**: `add`, `nand`
- **I-Type**: `lw`, `sw`, `beq`
- **J-Type**: `jalr`
- **O-Type**: `halt`, `noop`
- **Fill-Type**: `.fill`

## Features

- **Assembly Parsing**: Read assembly code from input files.
- **Machine Code Conversion**: Translate assembly code into binary and decimal machine code.
- **Label Mapping**: Automatically map labels within the assembly code.
- **File I/O Support**: Read assembly code from a file and output machine code to files.
- **Error Handling**: Provides error checking and informative output for debugging issues in the assembly code.

[!NOTE]This assembler only works for the instructions listed above. Make sure your assembly code matches the supported format!

