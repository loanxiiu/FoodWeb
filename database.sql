INSERT INTO food_web.brand (id, name)
VALUES
    (1, 'Vermicelli'),
    (2, 'PHO'),
    (3, 'Noodles'),
    (4, 'Juice'),
    (5, 'Rice');


INSERT INTO `food_web`.`users`
(`id`, `address`, `email`, `image`, `name`, `password`, `phone`, `role`)
VALUES
    ('1', 'Ha Noi', 'loanxinhiu@gmailcom', '/assets/images/avatar/z5118681383920_86120dedc4aa28e27194e3a8388a2026.jpg', 'Loan xinh iu', '1234', '0983638836', 'Admin'),
    ('2', 'Thanh Hoa', 'lien@gmailcom', '/assets/images/avatar/lien.jpg', 'Trinh Thi Lien', '123d4', '0932876256', 'Customer'),
    ('3', 'Nam Dinh', 'huyen@gmailcom', '/assets/images/avatar/huyen.jpg', 'Pham Thi Huyen', '123d4', '0932876256', 'Customer'),
    ('4', 'Hung Yen', 'quynh@gmailcom', '/assets/images/avatar/quynh.jpg', 'Vu Thuy Quynh', '123d4', '0932876236', 'Customer'),
    ('5', 'Ha Noi', 'luongcamly@gmailcom', '/assets/images/avatar/ly.jpg', 'Luong Cam Ly', '123d4', '0932876236', 'Customer');


INSERT INTO `food_web`.`food` (`brand_id`, `id`, `price`, `quantity`, `description`, `images`, `location`, `name`)
VALUES
    ('1', '001', '70000', '100', 'Bún bò Huế là món ăn đặc sản của Huế, gồm bún gạo ăn kèm với thịt bò (hoặc chân giò heo), nước dùng đậm đà, cay nồng từ sả và ớt, cùng rau sống và gia vị, mang đến hương vị đặc trưng và hấp dẫn.', '/assets/images/foods/bunbohue.jpg', 'TP.HCM', 'Bún bò Huế'),
    ('3', '002', '60000', '30', 'Mỳ trộn là món ăn gồm mì (thường là mì gạo hoặc mì trứng) trộn với các nguyên liệu như thịt, rau, trứng, và gia vị, tạo thành một món ăn đậm đà, thường có nước sốt để tăng thêm hương vị.', '/assets/images/foods/mytron.jpg', 'Hà Nội', 'Mỳ trộn'),
    ('1', '003', '75000', '120', 'Bún thập cẩm là món ăn Việt Nam gồm bún gạo ăn kèm với nhiều loại topping như thịt heo, tôm, chả, rau sống và nước mắm pha, tạo nên sự kết hợp đa dạng và đầy hương vị.', '/assets/images/foods/bunthapcam.jpg', 'TP.HCM', 'Bún thập cẩm'),
    ('4', '004', '55000', '16', 'Nước ép dưa hấu là đồ uống mát lạnh, ngọt tự nhiên, được làm từ dưa hấu tươi ép, giúp giải nhiệt và bổ dưỡng trong những ngày hè.', '/assets/images/foods/epduahau.jpg', 'Hà Nội', 'Nước ép dưa hấu'),
    ('1', '005', '68000', '21', 'Bún chả là món ăn đặc trưng của Việt Nam, gồm bún tươi ăn kèm thịt nướng (thịt heo hoặc thịt bò), rau sống, chả viên, nước mắm chua ngọt.', '/assets/images/foods/buncha.jpg', 'TP.HCM', 'Bún chả'),
    ('2', '006', '80000', '9', 'Phở bò là món canh đặc trưng của Việt Nam, gồm nước dùng trong, ngọt, nấu từ xương bò, ăn kèm với bánh phở, thịt bò thái lát mỏng, và gia vị như hành, ngò, chanh, ớt, và nước mắm.', '/assets/images/foods/phobo.jpg', 'Hà Nội', 'Phở bò'),
    ('2', '007', '75000', '200', 'Phở gà là món canh nổi tiếng của Việt Nam, gồm nước dùng trong, thơm, nấu từ gà, ăn kèm với bánh phở, hành, ngò và gia vị như chanh, ớt, và nước mắm.', '/assets/images/foods/phoga.jpg', 'Hà Nội', 'Phở gà'),
    ('5', '008', '70000', '5', 'Cơm tấm là món ăn đặc trưng miền Nam Việt Nam, gồm cơm được nấu từ gạo tấm, thường ăn kèm với sườn nướng, bì, chả, hoặc trứng ốp la, và có thêm dưa chua hoặc nước mắm pha.', '/assets/images/foods/comtam.jpg', 'Hà Nội', 'Cơm tấm'),
    ('5', '009', '60000', '29', 'Cơm rang là món ăn được làm từ cơm nguội, xào cùng với dầu, gia vị, thịt, rau hoặc trứng, mang hương vị thơm ngon và đậm đà.', '/assets/images/foods/comrang.jpg', 'Hà Nội', 'Cơm rang'),
    ('4', '010', '65000', '50', 'Nước ép táo là đồ uống từ táo tươi, được ép lấy nước, ngọt tự nhiên và thơm mát, bổ dưỡng.', '/assets/images/foods/eptao.jpg', 'Hà Nội', 'Nước ép táo'),
    ('3', '011', '55000', '2', 'Mỳ cay là món ăn Hàn Quốc gồm mì sợi trong nước dùng cay, thường kèm thịt, rau và gia vị đặc trưng.', '/assets/images/foods/mycay.jpg', 'Hà Nội', 'Mỳ cay KOBE'),
    ('1', '012', '80000', '40', 'Bún hải sản là món ăn Việt gồm bún gạo, tôm, mực, cá, nghêu, sò trong nước dùng ngọt, thơm.', '/assets/images/foods/bunhaisan.jpg', 'TP.HCM', 'Bún hải sản');



// bảng customer thì vào postman : POST http://localhost:8080/food_web/customers