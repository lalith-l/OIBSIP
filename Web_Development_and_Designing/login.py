import json
import hashlib
import os
import getpass
from datetime import datetime

class AuthSystem:
    def __init__(self):
        self.users_file = "users.json"
        self.current_user = None
        self.ensure_users_file()
    
    def ensure_users_file(self):
        """Create users file if it doesn't exist"""
        if not os.path.exists(self.users_file):
            with open(self.users_file, 'w') as f:
                json.dump({}, f)
    
    def load_users(self):
        """Load users from JSON file"""
        try:
            with open(self.users_file, 'r') as f:
                return json.load(f)
        except:
            return {}
    
    def save_users(self, users):
        """Save users to JSON file"""
        with open(self.users_file, 'w') as f:
            json.dump(users, f, indent=2)
    
    def hash_password(self, password):
        """Hash password using SHA-256"""
        return hashlib.sha256(password.encode()).hexdigest()
    
    def print_header(self, title):
        """Print beautiful header"""
        print("\n" + "="*50)
        print(f"{'':^15}{title:^20}{'':^15}")
        print("="*50)
    
    def print_separator(self):
        """Print separator line"""
        print("-" * 50)
    
    def register(self):
        """Register a new user"""
        self.print_header("USER REGISTRATION")
        
        users = self.load_users()
        
        while True:
            username = input("Enter username: ").strip()
            if not username:
                print("❌ Username cannot be empty!")
                continue
            
            if username in users:
                print("❌ Username already exists!")
                continue
            
            break
        
        while True:
            email = input("Enter email: ").strip()
            if not email or '@' not in email:
                print("❌ Please enter a valid email!")
                continue
            
            email_exists = any(user_data['email'] == email for user_data in users.values())
            if email_exists:
                print("❌ Email already registered!")
                continue
            
            break
        
        while True:
            password = getpass.getpass("Enter password (min 6 chars): ")
            if len(password) < 6:
                print("❌ Password must be at least 6 characters!")
                continue
            
            confirm_password = getpass.getpass("Confirm password: ")
            if password != confirm_password:
                print("❌ Passwords do not match!")
                continue
            
            break
        
        users[username] = {
            'email': email,
            'password': self.hash_password(password),
            'registered_at': datetime.now().isoformat(),
            'login_count': 0
        }
        
        self.save_users(users)
        print("✅ Registration successful!")
        print(f"Welcome {username}! You can now login.")
    
    def login(self):
        """Login user"""
        self.print_header("USER LOGIN")
        
        users = self.load_users()
        
        if not users:
            print("❌ No users registered yet!")
            return False
        
        username = input("Enter username: ").strip()
        password = getpass.getpass("Enter password: ")
        
        if username not in users:
            print("❌ Invalid username!")
            return False
        
        if users[username]['password'] != self.hash_password(password):
            print("❌ Invalid password!")
            return False
        
        # Update login count and set current user
        users[username]['login_count'] += 1
        users[username]['last_login'] = datetime.now().isoformat()
        self.save_users(users)
        
        self.current_user = {
            'username': username,
            'email': users[username]['email'],
            'login_count': users[username]['login_count'],
            'registered_at': users[username]['registered_at'],
            'login_time': datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
        
        print("✅ Login successful!")
        print(f"Welcome back, {username}!")
        return True
    
    def show_dashboard(self):
        """Show secured dashboard"""
        if not self.current_user:
            print("❌ Please login first!")
            return
        
        self.print_header("SECURED DASHBOARD")
        
        print(f"👤 Username: {self.current_user['username']}")
        print(f"📧 Email: {self.current_user['email']}")
        print(f"🔢 User ID: {abs(hash(self.current_user['username'])) % 10000}")
        print(f"📅 Registered: {self.current_user['registered_at'][:10]}")
        print(f"🕐 Login Time: {self.current_user['login_time']}")
        print(f"📊 Total Logins: {self.current_user['login_count']}")
        
        self.print_separator()
        print("🔐 PROTECTED FEATURES:")
        print("• ✅ View profile information")
        print("• ✅ Access exclusive content")
        print("• ✅ Manage account settings")
        print("• ✅ Secure data access")
        print("• ✅ Premium features unlocked")
        
        self.print_separator()
        print("🎉 Congratulations! You have successfully accessed the secured area!")
    
    def logout(self):
        """Logout current user"""
        if self.current_user:
            username = self.current_user['username']
            self.current_user = None
            print(f"✅ {username} logged out successfully!")
        else:
            print("❌ No user is currently logged in!")
    
    def show_menu(self):
        """Show main menu"""
        self.print_header("AUTHENTICATION SYSTEM")
        
        if self.current_user:
            print(f"👤 Logged in as: {self.current_user['username']}")
            print("\n1. 🏠 Dashboard (Secured Page)")
            print("2. 🚪 Logout")
            print("3. ❌ Exit")
        else:
            print("👤 Not logged in")
            print("\n1. 🔐 Login")
            print("2. 📝 Register")
            print("3. ❌ Exit")
        
        self.print_separator()
    
    def run(self):
        """Main application loop"""
        print("🎯 Welcome to the Python Authentication System!")
        print("💡 This system uses only built-in Python modules (no pip required)")
        
        while True:
            self.show_menu()
            
            try:
                choice = input("Enter your choice (1-3): ").strip()
                
                if not self.current_user:
                    if choice == '1':
                        if self.login():
                            input("\nPress Enter to continue...")
                    elif choice == '2':
                        self.register()
                        input("\nPress Enter to continue...")
                    elif choice == '3':
                        print("👋 Thank you for using the Authentication System!")
                        break
                    else:
                        print("❌ Invalid choice! Please enter 1, 2, or 3.")
                else:
                    if choice == '1':
                        self.show_dashboard()
                        input("\nPress Enter to continue...")
                    elif choice == '2':
                        self.logout()
                        input("\nPress Enter to continue...")
                    elif choice == '3':
                        self.logout()
                        print("👋 Thank you for using the Authentication System!")
                        break
                    else:
                        print("❌ Invalid choice! Please enter 1, 2, or 3.")
            
            except KeyboardInterrupt:
                print("\n\n👋 Application terminated by user.")
                break
            except Exception as e:
                print(f"❌ An error occurred: {e}")
                input("Press Enter to continue...")

def demo():
    """Demo function showing the system capabilities"""
    print("🎬 DEMO MODE - Sample User Creation")
    print("Creating a demo user for testing...")
    
    auth = AuthSystem()
    users = auth.load_users()
    
    if 'demo' not in users:
        users['demo'] = {
            'email': 'demo@example.com',
            'password': auth.hash_password('demo123'),
            'registered_at': datetime.now().isoformat(),
            'login_count': 0
        }
        auth.save_users(users)
        print("✅ Demo user created!")
        print("Username: demo")
        print("Password: demo123")
        print()
    
    return auth

if __name__ == "__main__":
    print("🚀 Python Authentication System")
    print("=" * 40)
    print("1. Run Normal Mode")
    print("2. Run Demo Mode (creates demo user)")
    print("=" * 40)
    
    try:
        mode = input("Choose mode (1-2): ").strip()
        
        if mode == '2':
            auth_system = demo()
        else:
            auth_system = AuthSystem()
        
        auth_system.run()
        
    except KeyboardInterrupt:
        print("\n👋 Goodbye!")
    except Exception as e:
        print(f"❌ Error: {e}")