using Microsoft.Azure.Mobile.Server;

namespace MobileApp99Service.DataObjects
{
    public class TodoItem : EntityData
    {
        public string Text { get; set; }

        public bool Complete { get; set; }
    }
}