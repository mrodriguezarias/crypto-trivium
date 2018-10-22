import tkinter as tk
from filepicker import FilePicker

class Form(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.elements = [
            {"name": "key", "label": "Clave de cifrado", "component": tk.Entry(self)},
            {"name": "input-file", "label": "Archivo de entrada", "component": FilePicker(self)},
            {"name": "output-file", "label": "Archivo de salida", "component": FilePicker(self, save=True)},
            {"name": "output-bits", "label": "Bits de salida", "component": tk.Entry(self)},
        ]
        self.create_interface()

    def create_interface(self):
        self.grid_columnconfigure(1, weight=1)
        self.grid(pady=5)

        for i, e in enumerate(self.elements):
            tk.Label(self, text=e["label"]+":").grid(row=i+1, column=0, sticky=tk.E, padx=(10,0), pady=5)
            e["component"].grid(row=i+1, column=1, sticky=tk.W+tk.E, padx=(2.5,10), pady=5)

        self.elements[0]["component"].focus()
