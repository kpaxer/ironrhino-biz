--customer
alter table customer drop column nameAsPinyin;
alter table customer drop column nameAsPinyinAbbr;
update customer set regionId = floor(1+ (rand() * 3000));
update customer set address = substring(address,1,2);
update customer set name = concat(substring(name,1,2),substring(name,4)) where char_length(name)>4;
update customer set name = concat(substring(name,1,1),substring(name,3)) where char_length(name)>2;
update customer set name = reverse(name) where char_length(name)=2;
update customer set linkman = concat(substring(linkman,1,1),substring(linkman,3)) where char_length(linkman)>2;
update customer set linkman = reverse(linkman) where char_length(linkman)=2;
update customer set mobile = null;
update customer set phone = null;
update customer set fax = null;
update customer set memo = null;

--employee
update employee set name = concat(substring(name,1,1),substring(name,3)) where char_length(name)>2;
update employee set name = reverse(name) where char_length(name)=2;
update employee set name = concat(substring(name,1,1),substring(name,3)) where char_length(name)>2;
update employee set address = null;
update employee set phone = null;
update employee set memo = null;

--brand
update brand set name='太太乐' where id=1;
update brand set name='步步高' where id=2;
update brand set name='苏仙' where id=3;

--product
update product set price = null;

--orders
update orderitem set price = floor(100+(price-100)*rand());
update orders o set grandTotal = (select sum(price*quantity) from orderitem oi where oi.orderId=o.id)-discount-freight;
update orders set memo = null;


--station
update station set regionId = floor(1+ (rand() * 3000));
update station set address = substring(address,1,2);
update station set name = concat(substring(name,1,char_length(name)-5),substring(name,-4)) where char_length(name)>6;
update station set name = concat(substring(name,1,2),substring(name,4)) where char_length(name)>4;
update station set name = concat(substring(name,1,1),substring(name,3)) where char_length(name)>2;
update station set name = reverse(name) where char_length(name)=2;
update station set destination = '全省';
update station set linkman = null;
update station set mobile = null;
update station set phone = null;
update station set fax = null;
update station set memo = null;

--stuff
update stuff set name = replace(name,'湘陵','');
update stuff set name = replace(name,'云阳','');

--user
update user set name = concat(substring(name,1,1),substring(name,3)) where char_length(name)>2;
update user set name = reverse(name) where char_length(name)=2;

--setting
update common_setting set value='食品调料有限公司' where skey='company.name';
delete from common_setting where skey!='company.name';


optimize table product;
optimize table plan;
optimize table customer;
optimize table orders;
optimize table orderitem;