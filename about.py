import tkinter as tk

class AboutDialog(object):

    def __init__(self, parent):
        root = self.root = tk.Toplevel(parent)
        self.parent = parent

        self.position(210, 190)
        self.root.resizable(False, False)
        root.after(10, lambda: root.grab_set_global())

        root.title("Acerca de")

        frame = tk.Frame(root)
        frame.pack(ipadx=10, ipady=2)

        msg = """Trivium Cipher
v1.0

Realizado por:
Bruno, Martín
Garcia, Santiago
Nosenzo, Alejandro
Rodríguez Arias, Mariano"""
        message = tk.Label(frame, text=msg)
        message.pack(padx=8, pady=4)

        bottom = tk.Frame(frame)
        bottom.pack(padx=2, pady=2)

        btn = tk.Button(bottom, width=3, text="OK", command=self.quit_action)
        btn.pack(side=tk.LEFT, padx=10)

        btn.bind("<Return>", func=self.quit_action)
        btn.bind("<KP_Enter>", func=self.quit_action)
        btn.bind("<Escape>", func=self.quit_action)
        btn.focus_set()

        root.protocol("WM_DELETE_WINDOW", self.quit_action)
        root.mainloop()
        root.destroy()

    def quit_action(self, event=None):
        self.root.quit()

    def position(self, width, height):
        x = self.parent.winfo_x() + (self.parent.winfo_width() - width) // 2
        y = self.parent.winfo_y() + (self.parent.winfo_height() - height) // 2 - 20
        self.root.geometry("{}x{}+{}+{}".format(width, height, x, y))
