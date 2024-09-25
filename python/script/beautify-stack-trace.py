import argparse

def formatLine(input):
    input = input.replace('\\n', '\n')
    input = input.replace('\\t', '')
    print(input)

if __name__ == '__main__':
    # Define the parser
    parser = argparse.ArgumentParser(description='Beautify stack trace')

    parser.add_argument('--filename', action='store', dest='filename', default='')
    parser.add_argument('--filecontent', action='store', dest='filecontent', default='')

    args = parser.parse_args()

    filename = args.filename

    filecontent = args.filecontent

    if filecontent == '':
        file = open(filename, 'r')
        for line in file:
            formatLine(line)
    else:
        lines = filecontent.split("\n")
        for line in lines:
            formatLine(line)

