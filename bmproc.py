import imghdr

class BMProc:
	HEADER_SIZE = 54

	@staticmethod
	def is_bmp(path):
		return imghdr.what(path) == "bmp"

	@classmethod
	def extract_header(cls, content):
		return content[:cls.HEADER_SIZE], content[cls.HEADER_SIZE:]
