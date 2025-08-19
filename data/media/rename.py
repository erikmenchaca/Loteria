import os
import re

# This data structure maps the Spanish names found in your filenames
# to the correct card number.
CARD_NAME_TO_NUMBER = {
    'gallo': 1, 'rooster': 1,
    'diablito': 2, 'devil': 2,
    'dama': 3,
    'catrin': 4, 'dandy': 4,
    'paraguas': 5, 'umbrella': 5,
    'sirena': 6,
    'escalera': 7,
    'botella': 8,
    'barril': 9,
    'arbol': 10, 'tree': 10,
    'melon': 11,
    'valiente': 12,
    'gorrito': 13,
    'muerte': 14, 'death': 14,
    'pera': 15,
    'bandera': 16,
    'bandolon': 17,
    'violoncello': 18,
    'garza': 19,
    'pajaro': 20,
    'mano': 21,
    'bota': 22,
    'luna': 23, 'moon': 23,
    'cotorro': 24, 'parrot': 24,
    'borracho': 25, 'drunk': 25,
    'negrito': 26,
    'corazon': 27, 'heart': 27,
    'sandia': 28,
    'tambor': 29,
    'camaron': 30,
    'jaras': 31,
    'musico': 32,
    'arana': 33,
    'soldado': 34,
    'estrella': 35,
    'cazo': 36,
    'mundo': 37,
    'apache': 38,
    'nopal': 39,
    'alacran': 40,
    'rosa': 41,
    'calavera': 42,
    'campana': 43,
    'cantarito': 44,
    'venado': 45,
    'sol': 46,
    'corona': 47,
    'chalupa': 48,
    'pino': 49,
    'pescado': 50,
    'palma': 51,
    'maceta': 52,
    'arpa': 53,
    'rana': 54
}

def find_card_number(filename):
    """
    Tries to find the card number from a filename.
    """
    base_name = os.path.splitext(filename)[0].lower().replace('_', ' ').replace('-', ' ')
    
    # Strategy 1: Check if the filename starts with a number
    match = re.match(r'^(\d+)', base_name)
    if match:
        return int(match.group(1))

    # Strategy 2: Check for a Spanish or English name match
    for name, number in CARD_NAME_TO_NUMBER.items():
        if name in base_name:
            return number
            
    return None

def rename_files_for_javafx(directory="."):
    """
    Iterates over files and renames them to the format 'NUMBER.png'.
    """
    print(f"Scanning for .png files to rename for JavaFX...")
    files_renamed = 0
    
    for filename in os.listdir(directory):
        if filename.lower().endswith('.png'):
            card_number = find_card_number(filename)
            
            if card_number:
                new_filename = f"{card_number}.png"
                
                old_path = os.path.join(directory, filename)
                new_path = os.path.join(directory, new_filename)
                
                if old_path == new_path:
                    continue # Already correctly named
                
                try:
                    os.rename(old_path, new_path)
                    print(f"✅ Renamed '{filename}' -> '{new_filename}'")
                    files_renamed += 1
                except OSError as e:
                    print(f"❌ Error renaming '{filename}': {e}")
            else:
                print(f"⚠️ Could not find a number for '{filename}'. Skipping.")
                
    print(f"\nFinished. Renamed {files_renamed} files.")

if __name__ == "__main__":
    rename_files_for_javafx()
