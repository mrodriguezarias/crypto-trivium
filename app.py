#!/usr/bin/env python3
import tkinter as tk
from os import system
from platform import system as platform
from form import Form
from about import AboutDialog
from trivium import Trivium
from pathlib import Path
from tkinter.messagebox import showwarning, showinfo
from math import ceil

class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master.title("Trivium")
        self.size(480, 260)
        self.bring_to_front()
        self.focus()
        self.create_interface()

    def create_interface(self):
        self.create_form()
        self.create_footer()

    def create_form(self):
        self.form = Form(self.master)
        self.form.pack(fill=tk.BOTH, expand=True, pady=10)

    def create_footer(self):
        self.footer = tk.Frame(bd=1, relief=tk.SUNKEN)

        self.runbtn = tk.Button(self.footer, text="Ejecutar", state=tk.ACTIVE, command=self.run_event_handler, takefocus=0)
        self.master.bind("<Return>", self.run_event_handler)
        self.master.bind("<KP_Enter>", self.run_event_handler)
        self.runbtn.pack(side="right", padx=10, pady=10)

        self.aboutbtn = tk.Button(self.footer, text="Acerca de", command=self.about_event_handler, takefocus=0)
        self.aboutbtn.pack(side="left", padx=10, pady=10)

        self.footer.pack(side="bottom", fill="x", expand=False)

    def run_event_handler(self, event=None):
        e = self.form.submit()
        if not e: return

        bits = int(e["output-bits"]) if e["output-bits"] else 0
        message = Path(e["input-file"]).read_bytes() if e["input-file"].strip() else None

        trivium = Trivium(e["key"], e["iv"])
        cipher = trivium.process(message, bits)
        written = Path(e["output-file"]).write_bytes(cipher)

        if message is not None and (not bits and len(message) != len(cipher) or bits and ceil(bits / 8) != len(cipher)):
            showwarning("Alerta", "Ocurrió un problema al procesar el archivo de entrada.")
        elif written != len(cipher):
            showwarning("Alerta", "Ocurrió un problema al intentar escribir el archivo de salida.")
        else:
            showinfo("Éxito", "El archivo se procesó correctamente.")


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
