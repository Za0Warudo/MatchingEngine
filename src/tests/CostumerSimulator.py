import random

# limit = [0, limit], market = [limit, market], peg =[market, 1]
limit_proportion = 0.5
market_proportion = 0.78
# buy = [0, side], sell = [side, 1]
side_proportion = 0.5

class PriceGenerator:

    def __init__(self, mass_center, gap, std):
        self.buy_mean = mass_center - gap
        self.sell_mean = mass_center + gap
        self.std = std

    def buy (self):
        return round(random.normalvariate(self.buy_mean, self.std), 2)

    def sell (self):
        return round(random.normalvariate(self.sell_mean, self.std), 2)


if __name__ == "__main__":
    file_name = input("File name: ")
    seed = int(input("Seed: "))
    output_size = int(input("Output size: "))
    mass_center = float(input("Mass center: "))
    gap = float(input("Gap: "))
    std = float(input("Standard deviation: "))

    random.seed(seed)

    generator = PriceGenerator(mass_center, gap, std)
    with open(file_name, "w") as file:
        for i in range(output_size):
            rand = random.random()
            order_type = "limit" if rand < limit_proportion else ("market" if rand < market_proportion else "peg")
            side = "buy" if random.random() < 0.5 else "sell"
            price = 0
            if order_type == "limit":
                is_bid_or_offer = True if random.random() < 0.2 else False
                if is_bid_or_offer:
                    price = "bid" if random.random() < 0.5 else "offer"
                else :
                    price = generator.buy() if side == "buy" else generator.sell()
            elif order_type == "peg":
                price = "bid" if random.random() < 0.5 else "offer"

            quantity = random.randint(15, 400)
            if order_type == "market":
                file.write(str(order_type) + " " + str(side) + " " + str(quantity) + "\n")
            elif order_type == "peg":
                file.write(str(order_type) + " " + str(price) + " " + str(side) + " " + str(quantity) + "\n")
            elif type(price) is str:
                file.write(str(order_type) + " " + str(price) + " " + str(side) + " " + str(quantity) + "\n")
            else:
                file.write(str(order_type) + " " + str(side) + " " + str(price) + " " + str(quantity) + "\n")