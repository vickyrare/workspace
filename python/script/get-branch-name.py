import argparse

if __name__ == '__main__':
    # Define the parser
    parser = argparse.ArgumentParser(description='Get git branch name')

    parser.add_argument('--ticket-desc', action='store', dest='ticket_desc', default='')

    args = parser.parse_args()

    ticket_desc = args.ticket_desc

    ticket_desc = ticket_desc.replace(': ', '-')
    ticket_desc = ticket_desc.replace(' ', '-')
    ticket_desc = ticket_desc.replace('(', '-')
    ticket_desc = ticket_desc.replace(')', '-')
    ticket_desc = ticket_desc.replace('--', '-')
    ticket_desc = ticket_desc.lower()
    ticket_desc = ticket_desc.replace('swaservices', 'washarif/' + 'swaservices'.upper())

    ticket_desc = ticket_desc.rstrip('-')

    print(ticket_desc)