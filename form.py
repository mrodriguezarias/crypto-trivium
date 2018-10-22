import tkinter as tk
from filepicker import FilePicker
from tkinter.messagebox import showwarning
from os.path import isfile, dirname, isdir

class Form(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.elements = [
            {"name": "key", "label": "Clave de cifrado", "component": tk.Entry(self)},
            {"name": "iv", "label": "Valor inicial", "component": tk.Entry(self)},
            {"name": "input-file", "label": "Archivo de entrada", "component": FilePicker(self)},
            {"name": "output-file", "label": "Archivo de salida", "component": FilePicker(self, save=True)},
            {"name": "output-bits", "label": "Bits de salida", "component": tk.Entry(self)},
        ]
        self._create_interface()

    def _create_interface(self):
        self.grid_columnconfigure(1, weight=1)
        self.grid(pady=5)

        for i, e in enumerate(self.elements):
            tk.Label(self, text=e["label"]+":").grid(row=i+1, column=0, sticky=tk.E, padx=(10,0), pady=5)
            e["component"].grid(row=i+1, column=1, sticky=tk.W+tk.E, padx=(2.5,10), pady=5)

        self.elements[0]["component"].focus()

    def get(self):
        return {e["name"]: e["component"].get() for e in self.elements}

    def _warn(self, msg):
        showwarning("Alerta", msg)

    def _validate(self, e):
        validations = [
            (lambda: e["key"].strip(), "La clave no puede quedar vacía"),
            (lambda: e["output-file"].strip(), "El archivo de salida no puede quedar vacío"),
            (lambda: not e["input-file"].strip() or isfile(e["input-file"]), "El archivo de entrada no existe o no puede leerse"),
            (lambda: not dirname(e["output-file"]) or isdir(dirname(e["output-file"])), "El archivo de salida no puede ser creado en esa ruta"),
            (lambda: not e["output-bits"].strip() or e["output-bits"].isnumeric() and int(e["output-bits"]) > 0, "El valor para bits de salida debe ser un número entero positivo"),
        ]

        for assertion, error in validations:
            if not assertion():
                showwarning("Alerta", error + ".")
                return False

        return True

    def submit(self):
        e = self.get()
        if not self._validate(e): return None
        return e
