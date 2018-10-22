import tkinter as tk
from tkinter.filedialog import askopenfilename, asksaveasfilename

class FilePicker(tk.Frame):
    def __init__(self, parent=None, save=False, *args, **kwargs):
        tk.Frame.__init__(self, parent)
        self.save = save
        self.entry = tk.Entry(self, *args, **kwargs)
        self.button = tk.Button(self, text="Seleccionar…", command=self._pick_event, takefocus=0)
        self.entry.pack(side="left", fill="both", expand=True)
        self.button.pack(side="right", padx=(2.5,0))

    def _pick_event(self):
        filename = asksaveasfilename() if self.save else askopenfilename()
        if not filename: return
        self.entry.delete(0, tk.END)
        self.entry.insert(0, filename)
        self.entry.focus()
        self.entry.xview(tk.END)

    def get(self):
        return self.entry.get()
