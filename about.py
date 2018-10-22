import tkinter as tk

class AboutDialog(object):

    def __init__(self, parent):
        root = self.root = tk.Toplevel(parent)
        self.parent = parent

        root.title("Acerca de")
        self.size(240, 180)
        root.after(10, lambda: root.grab_set_global())

        frm_1 = tk.Frame(root)
        frm_1.pack(ipadx=2, ipady=2)

        msg = """Trivium Cipher
v1.0

Realizado por:
Bruno, Martín
Garcia, Santiago
Nosenzo, Alejandro
Rodríguez Arias, Mariano"""
        message = tk.Label(frm_1, text=msg)
        message.pack(padx=8, pady=4)

        frm_2 = tk.Frame(frm_1)
        frm_2.pack(padx=2, pady=2)

        btn = tk.Button(frm_2, width=3, text="OK", command=self.quit_action)
        btn.pack(side=tk.LEFT, padx=10)

        btn.bind("<KeyPress-Return>", func=self.quit_action)
        btn.bind("<KeyPress-Escape>", func=self.quit_action)
        btn.focus_set()

        root.protocol("WM_DELETE_WINDOW", self.quit_action)
        root.mainloop()
        root.destroy()

    def quit_action(self, event=None):
        self.root.quit()

    def size(self, width, height):
        self.root.resizable(False, False)
        x = (self.parent.winfo_screenwidth() - width) // 2
        y = (self.parent.winfo_screenheight() - height) // 2
        self.root.geometry("{}x{}+{}+{}".format(width, height, x, y))
