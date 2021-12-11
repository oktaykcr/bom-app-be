alter table component rename column part_number to mouser_part_number;

alter table component
    add manufacturer_part_number varchar(255);

alter table component
    add data_sheet_url varchar(255);