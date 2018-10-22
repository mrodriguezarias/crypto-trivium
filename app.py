#!/usr/bin/env python3
import tkinter as tk
from os import system
from platform import system as platform
from form import Form
from about import AboutDialog

class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master.title("Trivium")
        self.size(480, 220)
        self.bring_to_front()
        self.focus()
        self.create_interface()

    def create_interface(self):
        Form(self.master).pack(fill=tk.BOTH, expand=True, pady=10)
        self.create_footer()

    def create_footer(self):
        self.footer = tk.Frame(bd=1, relief=tk.SUNKEN)

        self.runbtn = tk.Button(self.footer, text="Ejecutar", state=tk.ACTIVE, command=self.run_event_handler, takefocus=0)
        self.master.bind("<KeyPress-Return>", self.run_event_handler)
        self.runbtn.pack(side="right", padx=10, pady=10)

        self.aboutbtn = tk.Button(self.footer, text="Acerca de", command=self.about_event_handler, takefocus=0)
        self.aboutbtn.pack(side="left", padx=10, pady=10)

        self.footer.pack(side="bottom", fill="x", expand=False)

    def run_event_handler(self, event=None):
        pass

    def about_event_handler(self, event=None):
        AboutDialog(self.master)

    def size(self, width, height):
        self.master.minsize(width, height)
        x = (self.master.winfo_screenwidth() - width) // 2
        y = (self.master.winfo_screenheight() - height) // 2
        self.master.geometry("{}x{}+{}+{}".format(width, height, x, y))

    def bring_to_front(self):
        self.master.lift()
        self.master.attributes('-topmost', True)
        self.master.after_idle(self.master.attributes, '-topmost', False)

    def focus(self):
        if platform() == 'Darwin':
            system('''/usr/bin/osascript -e 'tell app "Finder" to set frontmost of process "Python" to true' ''')
        else:
            self.master.focus_force()

def main():
    root = tk.Tk()
    app = Application(root)
    app.mainloop()

if __name__ == "__main__":
    main()